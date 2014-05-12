package main.scala.com.hiq.scala.intermediate

/**
 * Monads
 */
object Monads {


  // Imagine the following

  case class Person(name: String) {
    def mother: Person.mother(this)
    def father: Person.father(this)
  }

  // You want to find both grand fathers for a Person
  def getBothGrandDads (person: Person) = {
    if (person != null) {
      if (person.mother != null) {
        //....
      }
      if (person.father != null) {
        //....
      }
    }
  }

  // And what happens if we do this?
  person.mother.father
  person.father.father


  // Can we do it another way? Enter Monads...

  sealed trait Maybe[+A] {

    // >>=
    def flatMap[B](f: A => Maybe[B]): Maybe[B]
  }

  case class Just[+A](a: A) extends Maybe[A] {
    override def flatMap[B](f: A => Maybe[B]) = f(a)
  }

  // Nothing in the Haskel example
  case object MaybeNot extends Maybe[Nothing] {
    override def flatMap[B](f: Nothing => Maybe[B]) = MaybeNot
  }

  case class Person(name: String) {
    def mother: Maybe[Person] = Person.mother(this)
    def father: Maybe[Person] = Person.father(this)
  }

  def bothGrandfathersForLoop(p: Person): Maybe[(Person, Person)] =
    for(
      m <- p.mother;
      fm <- m.father;
      f <- p.father;
      ff <- f.father)
    yield (fm, ff)


  // More examples

  def doSomeQueries: Reader[Connection, Int] = for {
    rs1 <- query("SELECT COUNT(*) FROM Foo")
    rs2 <- query("SELECT COUNT(*) FROM Bar")
  } yield rs1.getInt(1) + rs2.getInt(1)

  def doSomeQueries(conn: Connection) = (
    for {
      rs1 <- query("SELECT COUNT(*) FROM Foo")
      rs2 <- query("SELECT COUNT(*) FROM Bar")
    } yield rs1.getInt(1) + rs2.getInt(1)
  ).run(conn)
}
