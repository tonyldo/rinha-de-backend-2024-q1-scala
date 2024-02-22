package services

import com.typesafe.config.ConfigFactory
import domain.{ClientNotFoundException, Transaction, TransactionDescription, TransactionLimitException, TransactionType, TransactionValue}
import org.scalatest.funsuite.AnyFunSuite
import slick.jdbc.H2Profile.api._

import java.io.File
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ServicesSuite extends AnyFunSuite{
  test("Test transaction insert"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    assert(Await.result(TransactionService.insert(1,new Transaction(TransactionValue(1000),TransactionType.fromString("c"),TransactionDescription("trans1")),db), Duration(200, TimeUnit.MILLISECONDS)).balance == 1000)
  }

  test("Test transaction insert fail by client no exists"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    assertThrows[ClientNotFoundException](Await.result(TransactionService.insert(6,new Transaction(TransactionValue(1000001),TransactionType.fromString("d"),TransactionDescription("trans1")),db), Duration(200, TimeUnit.MILLISECONDS)))
  }

  test("Test transaction insert fail by transaction over limit"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    assertThrows[TransactionLimitException](Await.result(TransactionService.insert(1,new Transaction(TransactionValue(1000001),TransactionType.fromString("d"),TransactionDescription("trans1")),db), Duration(200, TimeUnit.MILLISECONDS)))
  }

  test("Balance Report"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    db.createSession()

    val result = Await.result(
         TransactionService.insert(1,new Transaction(TransactionValue(1111),TransactionType.fromString("c"),TransactionDescription("trans "+1)),db) zip
         TransactionService.insert(1,new Transaction(TransactionValue(2222),TransactionType.fromString("c"),TransactionDescription("trans "+2)),db) zip
         TransactionService.insert(1,new Transaction(TransactionValue(2222),TransactionType.fromString("d"),TransactionDescription("trans "+3)),db)
      , Duration(5000, TimeUnit.MILLISECONDS) )

    assert(result._2.balance==1111)

  }
}
