package services

import com.typesafe.config.ConfigFactory
import domain.{ClientNotFoundException, Transaction, TransactionDescription, TransactionLimitException, TransactionType, TransactionValue}
import infrastructure.adapters.dao.{ClientsDAO, TransactionsDAO}
import org.scalatest.funsuite.AnyFunSuite
import slick.jdbc.H2Profile
import slick.jdbc.JdbcBackend.Database

import java.io.File
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ServicesSuite extends AnyFunSuite{
  test("Test transaction insert"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientDao = ClientsDAO(H2Profile)
    val transactionDao = TransactionsDAO(H2Profile)
    val transactionService = TransactionService(H2Profile)
    assert(Await.result(transactionService.insert(1,new Transaction(TransactionValue(1000),TransactionType.fromString("c"),TransactionDescription("trans1")),clientDao,transactionDao,db), Duration(200, TimeUnit.MILLISECONDS)).balance.get == 1000)
  }

  test("Test transaction insert fail by client no exists"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientDao = ClientsDAO(H2Profile)
    val transactionDao = TransactionsDAO(H2Profile)
    val transactionService = TransactionService(H2Profile)
    assertThrows[ClientNotFoundException](Await.result(transactionService.insert(6,new Transaction(TransactionValue(1000001),TransactionType.fromString("d"),TransactionDescription("trans1")),clientDao,transactionDao,db), Duration(200, TimeUnit.MILLISECONDS)))
  }

  test("Test transaction insert fail by transaction over limit"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientDao = ClientsDAO(H2Profile)
    val transactionDao = TransactionsDAO(H2Profile)
    val transactionService = TransactionService(H2Profile)
    assertThrows[TransactionLimitException](Await.result(transactionService.insert(1,new Transaction(TransactionValue(1000001),TransactionType.fromString("d"),TransactionDescription("trans1")),clientDao,transactionDao,db), Duration(200, TimeUnit.MILLISECONDS)))
  }

  test("Balance Report"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientDao = ClientsDAO(H2Profile)
    val transactionDao = TransactionsDAO(H2Profile)
    val transactionService = TransactionService(H2Profile)
    db.createSession()

    try { Await.result(
      transactionService.insert(1,new Transaction(TransactionValue(1111),TransactionType.fromString("c"),TransactionDescription("trans "+1)),clientDao,transactionDao,db) zip
        transactionService.insert(1,new Transaction(TransactionValue(1111111111),TransactionType.fromString("d"),TransactionDescription("trans "+3)),clientDao,transactionDao,db) zip
        transactionService.insert(1,new Transaction(TransactionValue(2222),TransactionType.fromString("c"),TransactionDescription("trans "+2)),clientDao,transactionDao,db) zip
        transactionService.insert(6,new Transaction(TransactionValue(2222),TransactionType.fromString("c"),TransactionDescription("trans "+2)),clientDao,transactionDao,db)
      , Duration(5000, TimeUnit.MILLISECONDS) )
    } catch {
      case _:Exception => println("ok")
    }

    val report = Await.result(transactionService.balanceReport(1,clientDao,transactionDao,db), Duration(5000, TimeUnit.MILLISECONDS) )

    assert(report.transactions.size ==2)
  }
}
