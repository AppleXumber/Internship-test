package dev.wayron.test.person

import dev.wayron.test.person.PersonServiceRepository.addPerson
import dev.wayron.test.person.PersonServiceRepository.getPersons
import dev.wayron.test.person.PersonServiceRepository.getPersonsSpendingInfos
import dev.wayron.test.person.PersonServiceRepository.removePerson
import dev.wayron.test.transactions.Transaction
import dev.wayron.test.transactions.TransactionServiceRepository
import dev.wayron.test.transactions.Type
import org.springframework.stereotype.Repository

/**
 * This is the `Service` and `Repository` to the Persons.
 * * The `object` indicates that it is a Singleton to our data, we can't just use a simple `Class`, because every `Class` would create a different repository with different data, and we need our data to be consistent.
 * * The Service has its own method of maneging data and `Id`s, using a `MutableList` and a `lastId` to take care of it.
 * @see Person
 * @see addPerson
 * @see getPersons
 * @see getPersonsSpendingInfos
 * @see removePerson
 * */
@Repository
object PersonServiceRepository {
  private val persons = mutableListOf<Person>()
  private var lastId = 0

  /**
   * Receives a person and adds it to the repository, increases the `lastId` and returns it.
   *
   * @param person The person to be added
   * @return `person` - The person added to the database.
   *
   * @see Person
   * */
  fun addPerson(person: Person): Person {
    val newPerson = Person(++lastId, person.name, person.age)
    persons.add(newPerson)
    return newPerson
  }

  /**
   * This function returns a list of all Persons stored on the database, with their ids, names and ages.
   * If there's no person, it returns an empty list.
   * @return `<List<Person>` containing every person registered on the database.
   * @see Person
   * */
  fun getPersons(): List<Person> = persons

  /** This function receives an id of a person registered on the database and returns with their names, ages, incomes, expanses and total net.
   * If the id does not exist on the database, throws a RuntimeException
   * @param id The id of the person to be found.
   * @return `Map<String, Any>` with the person's name, age, income, expenses and total net.
   * @throws RuntimeException if the person does not exist on the database.
   * @see Person
   * @see Transaction
   * */
  fun getSinglePersonSpendingInfos(id: Int): Map<String, Any> {
    val person = getPersons().find { it.id == id } ?: throw RuntimeException("Person not found")
    val personTransactions = TransactionServiceRepository.getTransactionsByPerson(id)
    val personExpanses = personTransactions.filter { it.type == Type.DESPESA }.sumOf { it.value }
    val personIncome = personTransactions.filter { it.type == Type.RECEITA }.sumOf { it.value }
    val personBalance = personIncome - personExpanses

    return mapOf(
      "personName" to person.name,
      "personAge" to person.age,
      "personIncome" to personIncome,
      "personExpanses" to personExpanses,
      "personBalance" to personBalance
    )
  }

  /** This function returns a `Map` with a list of every person stored with their names, ages, total incomes, total expanses and total net.
   * As well a sum of every person total income, every person expanses and total net.
   * If there's no person registered on the system, it returns an empty list and the other values as 0.0.
   * @return `Map<String, Any>` with every person with their data and a sum of every person expanses and total net.
   *
   * @see Person
   * @see Transaction
   * */
  fun getPersonsSpendingInfos(): Map<String, Any> {
    val persons = getPersons()
    val personsSpending = persons.map { getSinglePersonSpendingInfos(it.id) }
    val totalIncome = personsSpending.sumOf { it["personIncome"] as Double }
    val totalExpanses = personsSpending.sumOf { it["personExpanses"] as Double }
    val balance = totalIncome - totalExpanses

    return mapOf(
      "persons" to personsSpending,
      "totalIncome" to totalIncome,
      "totalExpanses" to totalExpanses,
      "balance" to balance
    )
  }

  /**
   * Removes the person with that id on the Person repository and deletes all the transactions which have the same personId on the TransactionRepository
   * @param id ID number of the person to be removed from the system
   * @see Person
   * @see Transaction
   * @see TransactionServiceRepository.removeTransactions
   * */
  fun removePerson(id: Int) {
    persons.removeIf { it.id == id }
    TransactionServiceRepository.removeTransactions(id)
  }
}