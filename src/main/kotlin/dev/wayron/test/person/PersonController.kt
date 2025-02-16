package dev.wayron.test.person

import dev.wayron.test.transactions.Transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Handles the `Person`s `Get`s, `Post`s and `Delete`s
 * @see Person
 * @see createPerson
 * @see deletePerson
 * @see getSpendingsByPerson
 * @see getPersons
 * @see getSpendings
 * */
@RestController
class PersonController {
  /**
   * This functions receives a JSON representation of `Person` and validates it.
   * Adds a new person to the repository and returns the created person.
   * Returns HTTP status of 201 (Created) if successful, 400 (Bad Request) if not.
   *
   * @param person The `Person` to be added to the repository, containing `name` and `age`.
   * @return `ResponseEntity<Person>` - with the status of 201 if successful or 400 if an error occurred.
   *
   * @throws RuntimeException if the person cannot be added due to invalid data.
   * @see Person
   * @see PersonServiceRepository.addPerson
   *
   * */
  @PostMapping("/persons")
  fun createPerson(@RequestBody person: Person): ResponseEntity<Person> {
    return try {
      val createdPerson = PersonServiceRepository.addPerson(person)
      ResponseEntity.status(HttpStatus.CREATED).body(createdPerson)
    } catch (e: RuntimeException) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }
  }

  /**
   * This function returns a list of all Persons stored on the database, with their ids, names and ages.
   * If there's no person, it returns an empty list.
   * @return `<ResponseEntity<List<Person>>` containing every person registered on the database.
   * @see Person
   * @see PersonServiceRepository.getPersons
   * */
  @GetMapping("/persons")
  fun getPersons(): ResponseEntity<List<Person>> {
    return ResponseEntity.status(HttpStatus.OK).body(PersonServiceRepository.getPersons())
  }

  /** This function receive an id and deletes the person registered on the database which has the same id, as well as the transactions the person have made.
   * @param id of the person to be deleted
   * @return `ResponseEntity<Nothing>` with the status of 204 (No Content) indicating the deletion.
   *
   * @see Person
   * @see PersonServiceRepository.removePerson
   * */
  @DeleteMapping("/persons/{id}")
  fun deletePerson(@PathVariable id: Int): ResponseEntity<Nothing> {
    PersonServiceRepository.removePerson(id)
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
  }

  /** This function receives an id of a person registered on the database and returns with their names, ages, incomes, expanses and total net.
   * If the id does not exist on the database, throws a RuntimeException
   * @param id The id of the person to be found.
   * @return `ResponseEntity<Map<String, Any>>` with the status of 200 (Ok) or 400 (Bad request) if the person does not exist.
   * @throws RuntimeException if the person does not exist on the database.
   * @see Person
   * @see PersonServiceRepository.getPersonsSpendingInfos
   * */
  @GetMapping("/persons/{id}/summary")
  fun getSpendingsByPerson(@PathVariable id: Int): ResponseEntity<Map<String, Any>> {
    return ResponseEntity.status(HttpStatus.OK).body(PersonServiceRepository.getSinglePersonSpendingInfos(id))
  }

  /** This function returns a `Map` with a list of every person stored with their names, ages, total incomes, total expanses and total net.
   * As well a sum of every person total income, every person expanses and total net.
   * If there's no person registered on the system, it returns an empty list and the other values as 0.0.
   * @return `ResponseEntity<Map<String, Any>>` with every person with their data, and a sum of every person expanses and total net.
   *
   * @see Person
   * @see Transaction
   * @see PersonServiceRepository.getPersonsSpendingInfos
   * */
  @GetMapping("/persons/summary")
  fun getSpendings(): ResponseEntity<Map<String, Any>> {
    return ResponseEntity.status(HttpStatus.OK).body(PersonServiceRepository.getPersonsSpendingInfos())
  }
}