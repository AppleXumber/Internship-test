package dev.wayron.test.transactions

import dev.wayron.test.person.Person
import dev.wayron.test.person.PersonServiceRepository
import org.springframework.stereotype.Repository

/**
 * This is the `Service` and `Repository` to the Transactions.
 * * The `object` indicates that it is a Singleton to our data, we can't just use a simple `Class`, because every `Class` would create a different repository with different data, and we need our data to be consistent.
 * * The Service has its own method of maneging data and `Id`s, using a `MutableList` and a `lastId` to take care of it.
 * */
@Repository
object TransactionServiceRepository {
  private val transactions = mutableListOf<Transaction>()
  private var lastId = 0

  /**
   * Receives a transaction, validates id, adds to the database and returns it.
   * If the person does not exist or if the transaction is of the type `RECEITA` if the person is less than 18 years old, throws a RuntimeException.
   *
   * @param transaction Transaction to be added.
   * @return `transaction` - Transaction added to the database.
   * @throws RuntimeException if the person does not exist or if the person is less than 18 years old with a `RECEITA` transaction
   * @see Transaction
   * @see Person
   * */
  fun addTransaction(transaction: Transaction): Transaction {
    val newTransaction =
      Transaction(++lastId, transaction.description, transaction.value, transaction.type, transaction.idPerson)

    val person = PersonServiceRepository.getPersons()
      .find { it.id == transaction.idPerson }
      ?: throw RuntimeException("Person does not exist on the database")

    if (person.age < 18 && transaction.type == Type.RECEITA
    ) throw RuntimeException("A person under 18 can't register a revenue")

    transactions.add(newTransaction)
    return newTransaction
  }

  /** Returns a list of every transaction on the database. If there's no transaction on the database, returns an empty list.
   * @return `transaction` Transactions on the database.
   * @see Transaction
   * */
  fun getTransactions() = transactions

  /** Receives an ID and removes every person transaction from the database whom have this ID.
   * @param personId The of the person which the transactions are being deleted.
   * @see Transaction
   * @see Person
   * */
  fun removeTransactions(personId: Int) {
    transactions.removeIf { it.idPerson == personId }
  }

  /** Receives an ID and returns every transaction which have the person's id matching it.
   * @param personId The id of the person to have the transactions find.
   * @return `List<Transaction>` The list of transactions that have that person's id registered.
   * @see Transaction
   * @see Person
   * */
  fun getTransactionsByPerson(personId: Int): List<Transaction> {
    return transactions.filter { it.idPerson == personId }
  }
}