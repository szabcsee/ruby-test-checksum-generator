
/**
  * Ruby Developer – Generate Checksum
Create a web application that generates a checksum for user input:
- The application has a single page with a form with a single text area input and a submit button
- Users should type in any long string into the text area
- When form submitted, a checksum should be generated based on the content of the text
area input and displayed to the user Checksum generating rules:
- Remove any character that is not in the English ABC, not space
- Create words which are 10 characters long, by joining the words
o E.g.: foo bar baz wibble fizzbuzz fizz buzz -> foobarbazw ibblefizzb uzzfizzbuz z
- Capitalize each word (E.g.: Each word should start with a capital letter and all other letter
should be lowercase)
- Upper case any vowel that is after two or more consonants and previous vowel is upper case.
o E.g.: Foobarbazw Ibblefizzb Uzzfizzbuz Z -> Foobarbazw IbblEfizzb UzzfIzzbUz Z
- Downcase any vowel that doesn’t match the above rule, do not down case any vowel which
is the first letter of a word.
- Checksum contains:
o Count of original words (7)
o Count of newly created words (4)
o Count of upper case vowels (5)
o Count of consonants (21)
o Length of original string (37)
▪ E.g.: 7-4-5-21-3
  */

package example
import scala.collection.mutable.StringBuilder

trait ChecksumFilter {
  def addChar(c: Char) : Unit

  def close(): String
}

class NilFilter extends ChecksumFilter {
  def addChar(c: Char) : Unit = {}

  def close(): String = ""
}

class SpecLetterCounter(nextFilter: ChecksumFilter) extends ChecksumFilter {
  var uppercaseVowels = 0
  var consonants = 0

  override def addChar(c: Char): Unit = {
      if (c != ' ') {
        if ("EUIOA".contains(c)) {
          uppercaseVowels += 1
        } else if (!"euioa".contains(c)) {
          consonants += 1
        }
      }
      nextFilter.addChar(c)
  }

  override def close(): String = nextFilter.close()
}

class WordCounter(nextFilter: ChecksumFilter) extends ChecksumFilter {
  var count = 0

  var isSpace = true

  override def addChar(c: Char): Unit = {
      if (isSpace) {
        if (c != ' ') {
          count += 1
          isSpace = false
        }
      } else {
        isSpace = c == ' '
      }
      nextFilter.addChar(c)
  }

  override def close(): String = nextFilter.close()
}

class EnglishFilter(nextFilter: ChecksumFilter) extends ChecksumFilter {

  override def addChar(c: Char): Unit =
    if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || c == ' ') {
      nextFilter.addChar(c)
    }

  override def close(): String = nextFilter.close()
}

class Joiner(nextFilter: ChecksumFilter) extends ChecksumFilter {
  val a = Array.fill(10)(' ')

  var index = 0

  override def addChar(c: Char): Unit = {
    if(index == a.length) {
      for (c <- a) nextFilter.addChar(c)
      nextFilter.addChar(' ')
      index = 0
    }
    if (c != ' ') {
      a(index) = c
      index += 1
    }
  }

  override def close(): String = {
    for (i <- 0 until index) {
      nextFilter.addChar(a(i))
    }
    nextFilter.close()
  }
}

class Capitalize(nextFilter: ChecksumFilter) extends ChecksumFilter {
  var isSpace = true

  override def addChar(c: Char): Unit = {
    if (c == ' ') {
      isSpace = true
      nextFilter.addChar(' ')
    } else {
      nextFilter.addChar(if (isSpace) c.toUpper else c.toLower)
      isSpace = false;
    }
  }

  override def close(): String = nextFilter.close()

}

object InsideCapitalize {
  val wordStart = 0
  val prevVowelUpper = 1
  val oneConsonants = 2
  val twoOrMoreConsonants = 3
  val skipState = 4
}

//  - Upper case any vowel that is after two or more consonants and previous vowel is upper case.
//  o E.g.: Foobarbazw Ibblefizzb Uzzfizzbuz Z -> Foobarbazw IbblEfizzb UzzfIzzbUz Z
class InsideCapitalize(nextFilter: ChecksumFilter) extends ChecksumFilter {

  abstract class State(nextFilter: ChecksumFilter) {

    private def isVowel(c: Char) = "euioa".contains(c.toLower)

    def addVowel(c: Char): Unit
    def addConsonant(c: Char): Unit
    def addSpace(): Unit

    def addChar(c: Char): Unit = {
      if (c == ' ') {
        addSpace()
      } else if (isVowel(c)) {
        addVowel(c)
      } else {
        addConsonant(c)
      }
    }
  }

  case class SkipState(nextFilter: ChecksumFilter) extends State(nextFilter) {
    def addConsonant(c: Char): Unit = {
      nextFilter.addChar(c)
    }

    def addVowel(c: Char): Unit = {
      nextFilter.addChar(c)
    }

    def addSpace(): Unit = {
      nextFilter.addChar(' ')
      state = WORD_START
    }
  }
  val SKIP_STATE = SkipState(nextFilter)

  case class OneCon(nextFilter: ChecksumFilter) extends State(nextFilter) {
    def addConsonant(c: Char): Unit = {
      nextFilter.addChar(c)
      state = TWO_OR_MORE_CON
    }

    def addVowel(c: Char): Unit = {
      nextFilter.addChar(c)
      state = SKIP_STATE
    }

    def addSpace(): Unit = {
      nextFilter.addChar(' ')
      state = WORD_START
    }
  }

  val ONE_CON = OneCon(nextFilter)

  case class TwoOrMoreCon(nextFilter: ChecksumFilter) extends State(nextFilter) {
    def addConsonant(c: Char): Unit = {
      nextFilter.addChar(c)
    }

    def addVowel(c: Char): Unit = {
      nextFilter.addChar(c.toUpper)
      state = PREV_VOWEL_UPPER
    }

    def addSpace(): Unit = {
      nextFilter.addChar(' ')
      state = WORD_START
    }
  }
  val TWO_OR_MORE_CON = TwoOrMoreCon(nextFilter)


  case class PrevVowelUpper(nextFilter: ChecksumFilter) extends State(nextFilter) {
    def addConsonant(c: Char): Unit = {
      nextFilter.addChar(c)
      state = ONE_CON
    }
    def addVowel(c: Char): Unit = {
      nextFilter.addChar(c)
      state = SKIP_STATE
    }

    def addSpace(): Unit = {
      nextFilter.addChar(' ')
      state = WORD_START
    }
  }

  val PREV_VOWEL_UPPER = PrevVowelUpper(nextFilter)

  case class WordStart(nextFilter: ChecksumFilter) extends State(nextFilter) {
    def addConsonant(c: Char): Unit = {
      nextFilter.addChar(c)
      state = SKIP_STATE
    }
    def addVowel(c: Char): Unit = {
      nextFilter.addChar(c.toUpper)
      state = PREV_VOWEL_UPPER
    }
    def addSpace(): Unit = {
      nextFilter.addChar(' ')
    }
  }

  val WORD_START = WordStart(nextFilter)

  var state: State = WORD_START

  override def addChar(c: Char): Unit = {
    state.addChar(c)
  }

  override def close(): String = nextFilter.close()

}

class Collector extends ChecksumFilter {

  private val sBuilder = new StringBuilder

  override def addChar(c: Char): Unit = sBuilder += c

  override def close(): String = sBuilder.toString()
}

object Checksum {

  def calc(s: String) = {
    val specLet = new SpecLetterCounter(new NilFilter)
    val w2 = new WordCounter(specLet)
    val w1 = new WordCounter(new EnglishFilter(new Joiner(new Capitalize(new InsideCapitalize(w2)))))
    s.foreach(w1.addChar(_))
    w1.close()
    s"${w1.count} - ${w2.count} - ${specLet.uppercaseVowels} - ${specLet.consonants} - ${s.length}"
  }

}
