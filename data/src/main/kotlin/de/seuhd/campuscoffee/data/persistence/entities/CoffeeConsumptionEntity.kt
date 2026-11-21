package de.seuhd.campuscoffee.data.persistence.entities

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * JPA entity of a consumption, which holds a [user], the coffee [count] and the balance in cents ([balanceCents]) of
 * that user. The balance can be negative, so the column has no check constraint. The migration
 * `V5__create_buy_coffee.sql` adds the column with a default of zero. The unique constraint
 * [USER_UNIQUE_CONSTRAINT] allows one row per user. [coffeeTimes] holds the time in UTC of each recent coffee that is
 * not undone, oldest first, in the table `coffee_times`, which the migration `V3__create_coffee_times_table.sql`
 * creates. The column `position` keeps the order. JPA loads the times together with the consumption, because the read
 * methods of the data adapter map the entity outside a transaction (see `CrudDataServiceImpl`).
 */

@jakarta.persistence.Entity
@Table(name = "coffee_consumptions")
class CoffeeConsumptionEntity : Entity() {
    @field:ManyToOne
    @field:JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @field:Column(name = "count")
    var count: Int? = null

    @field:ElementCollection(fetch = FetchType.EAGER)
    @field:CollectionTable(name = "coffee_times", joinColumns = [JoinColumn(name = "consumption_id")])
    @field:OrderColumn(name = "position")
    @field:Column(name = "added_at")
    var coffeeTimes: MutableList<LocalDateTime> = mutableListOf()

    @field:Column(name = "balance_cents")
    var balanceCents: Int? = null

    companion object {
        /** Name of the unique constraint on `user_id`. */
        const val USER_UNIQUE_CONSTRAINT = "uq_coffee_consumptions_user"
    }
}
