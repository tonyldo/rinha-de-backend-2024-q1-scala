package services

import com.typesafe.config.ConfigFactory
import domain.{ClientNotFoundException, Transaction, TransactionDescription, TransactionType, TransactionValue}
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
    assert(Await.result(TransactionService.insert(1,new Transaction(TransactionValue(1000),TransactionType.fromString("c"),TransactionDescription("trans1")),db), Duration(200, TimeUnit.MILLISECONDS)) == 1)
  }

  test("Test transaction insert fail"){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    assertThrows[ClientNotFoundException](Await.result(TransactionService.insert(6,new Transaction(TransactionValue(1000),TransactionType.fromString("c"),TransactionDescription("trans1")),db), Duration(200, TimeUnit.MILLISECONDS)))
  }
}
