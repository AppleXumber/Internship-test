package dev.wayron.test.person

/**The `Person` data class represents an individual with an ID, name and age.
 * @property id Unique identifier for the person
 * @property name Full name of the person
 * @property age Age of the person
*/
data class Person(
  val id: Int,
  var name: String,
  var age: Int
)
