package example

import org.scalatest._

class ChecksumSuite extends FunSuite with Matchers {
  test("joiner") {
    val c = new Collector
    val e = new Joiner(c)
    "foo bar baz wibble fizzbuzz fizz buzz".foreach(e.addChar(_))
    val res: String = e.close()
    assert(res === "foobarbazw ibblefizzb uzzfizzbuz z")
  }

  test("capitalize") {
    val c = new Collector
    val e = new Capitalize(c)
    "foO bAR bAZ wIBBLe FIZZBUzZ FiZz bUzz".foreach(e.addChar(_))
    val res: String = e.close()
    assert(res === "Foo Bar Baz Wibble Fizzbuzz Fizz Buzz")
  }

  test("InsideCapitalize") {
    val c = new Collector
    val e = new InsideCapitalize(c)
    "Foobarbazw Ibblefizzb Uzzfizzbuz Z".foreach(e.addChar(_))
    val res: String = e.close()
    assert (res === "Foobarbazw IbblEfizzb UzzfIzzbUz Z")
  }


  test("full checksum compute") {
    assert(Checksum.calc("foo bar baz wibble fizzbuzz fizz buzz") === "7 - 4 - 5 - 21 - 37")
  }
}
