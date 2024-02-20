package infrastructure.adapters.dao

import com.typesafe.config.ConfigFactory
import infrastructure.adapters.dao.entities.TransactionEntity
import org.scalatest.funsuite.AnyFunSuite
import slick.jdbc.H2Profile.api._

import java.io.File
import java.sql.SQLIntegrityConstraintViolationException
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random


class DatabaseSuite extends AnyFunSuite{

  test("Test Client Repository."){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientsTable = TableQuery[Clients]

    assert(Await.result(db.run(clientsTable.filter(_.id === 1).result), Duration(200, TimeUnit.MILLISECONDS)).head.id==1)
  }

  test("Test insert transactions."){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val transactionTable = TableQuery[Transactions]

    val insertTransactionQuery = transactionTable += new TransactionEntity(None,1,2000,'d',"test1")

    val insertTransactionQuery2 = transactionTable += new TransactionEntity(None,1,3000,'d',"test2")

    assert(Await.result(db.run((insertTransactionQuery andThen insertTransactionQuery2).transactionally), Duration(200, TimeUnit.MILLISECONDS)).leftSide==1)

    assert(Await.result(db.run(transactionTable.length.result), Duration(200, TimeUnit.MILLISECONDS))==2)

    val insertTransactionQuery3 = transactionTable += new TransactionEntity(None,1,4000,'d',"test3")

    val insertTransactionQuery4 = transactionTable += new TransactionEntity(None,1,5000,'e',"test4")

    assertThrows[SQLIntegrityConstraintViolationException](Await.result(db.run((insertTransactionQuery3 andThen insertTransactionQuery4).transactionally), Duration(200, TimeUnit.MILLISECONDS)))

    assert(Await.result(db.run(transactionTable.length.result), Duration(200, TimeUnit.MILLISECONDS))==2)
  }

  test("Test Clients updates"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientsTable = TableQuery[Clients]

    val updateClientBalance = clientsTable.filter(_.id === 3).map(_.balance).update(111111)

    val query = clientsTable.filter(_.id === 3).result

    val dbIOActionResult = updateClientBalance andThen query

    assert(Await.result(db.run(dbIOActionResult),Duration(200, TimeUnit.MILLISECONDS)).head.balance==111111)
  }

  test("Test get the 10 first transactions of a client"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val transactions = TableQuery[Transactions]

    for (i <- 1 to 100) yield {
      Await.result(db.run(transactions += new TransactionEntity(None, Random.between(1,5), 1000*i, if (i%2==0) 'd' else 'c',"trans " + i)),Duration(200, TimeUnit.MILLISECONDS))
    }

    val result = Await.result(db.run(transactions.filter(_.clientId === Random.between(1,5)).sorted(t=>t.creation.desc).take(10).result),Duration(200, TimeUnit.MILLISECONDS))

    assert( result.foldLeft((LocalDateTime.now().compareTo(result.head.creation)>=0,result.head.creation))((a,t)=>(a._1 && a._2.compareTo(t.creation)>=0,t.creation))._1 )
  }
}
