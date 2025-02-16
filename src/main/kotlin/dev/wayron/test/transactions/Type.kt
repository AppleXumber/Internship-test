package dev.wayron.test.transactions

/** Each transaction has two types:
 * `DESPESA` (expanse)
 * or
 * `RECEITA` (income)
 * @see Transaction
 * */
enum class Type {
  DESPESA,
  RECEITA
}