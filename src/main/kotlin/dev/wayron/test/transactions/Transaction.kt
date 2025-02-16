package dev.wayron.test.transactions

import dev.wayron.test.person.Person

/** The `Transaction` data class represents a unique transaction with its
 * ID, description, value, type and the ID of the person who made it.
 *
 * @property id Unique identifier for the transaction
 * @property description Description of the transaction
 * @property value How much is on the transaction
 * @property type The `Type` of the transaction. If it is an expanse or an income.
 * @property idPerson The ID of  the person responsible for the transaction.
 * @see Type
 * @see Person
 * */
data class Transaction(
  val id: Int,
  var description: String = "",
  var value: Double = 0.0,
  var type: Type,
  val idPerson: Int
)