package dev.wayron.test.transactions

import dev.wayron.test.person.Person
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**Handles the `Transactions` `Post`s and `Get`s
 * @see Transaction
 * @see createTransaction
 * @see getTransactions
 * */
@RestController
class TransactionController {

  /**
   * Receives a transaction, validates id, adds to the database and returns it.
   * If the person does not exist or if the transaction is of the type `RECEITA` if the person is less than 18 years old, throws a RuntimeException.
   *
   * @param transaction Transaction to be added.
   * @return `ResponseEntity<Transaction>` with the status 201 (Created) or 400 if an error occurred.
   * @throws RuntimeException if the person does not exist or if the person is less than 18 years old with a `RECEITA` transaction
   * @see Transaction
   * @see Person
   * @see TransactionServiceRepository.addTransaction
   * */
  @PostMapping("/transaction")
  fun createTransaction(@RequestBody transaction: Transaction): ResponseEntity<Transaction> {
    return try {
      val createdTransaction = TransactionServiceRepository.addTransaction(transaction)
      ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction)
    } catch (e: RuntimeException) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }
  }

  /** Returns a list of every transaction on the database. If there's no transaction on the database, returns an empty list.
   * @return `List<Transaction>` Transactions on the database.
   * @see Transaction
   * @see TransactionServiceRepository.getTransactions
   * */
  @GetMapping("/transaction")
  fun getTransactions(): ResponseEntity<List<Transaction>> {
    return ResponseEntity.status(HttpStatus.OK).body(TransactionServiceRepository.getTransactions())
  }
}