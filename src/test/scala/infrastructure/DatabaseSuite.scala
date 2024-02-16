package infrastructure

import com.typesafe.config.ConfigFactory
import infrastructure.adapters.dao.Clients
import infrastructure.adapters.dao.entities.ClientEntity
import org.scalatest.funsuite.AnyFunSuite
import slick.jdbc.H2Profile.api._

import java.io.File
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


class DatabaseSuite extends AnyFunSuite{

  test("Test Client Repository."){
    ConfigFactory.parseFile(new File("src/test/resources/application.conf"))
    val db = Database.forConfig("h2mem")
    val clientsTable = TableQuery[Clients]

    val result: Future[Seq[ClientEntity]] = db.run(clientsTable.filter(_.id === 1).result)

    val futureResult = result.map { s =>
      if (s.nonEmpty) println(s.head)
      else fail("Client repository test fail")
    }

    Await.result(futureResult, 1.seconds)
  }
}
