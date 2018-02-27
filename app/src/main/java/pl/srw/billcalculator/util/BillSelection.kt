package pl.srw.billcalculator.util

import pl.srw.billcalculator.db.Bill
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillSelection @Inject constructor() {

    private val selectedItems = mutableMapOf<Int, Bill>()

    fun select(position: Int, o: Bill) {
        selectedItems[position] = o
    }

    fun deselect(position: Int) {
        selectedItems.remove(position)
    }

    fun deselectAll() {
        selectedItems.clear()
    }

    fun isSelected(position: Int) = selectedItems.containsKey(position)

    fun isAnySelected() = selectedItems.isNotEmpty()

    fun getItems(): Collection<Bill> = selectedItems.values

    fun getPositionsReverseOrder() = selectedItems.keys.sortedDescending().toIntArray()

    fun onInsert(atPosition: Int) {
        val updated = mutableMapOf<Int, Bill>()
        val iterator = selectedItems.iterator()
        while (iterator.hasNext()) {
            val (position, bill) = iterator.next()
            if (position >= atPosition) {
                iterator.remove()
                updated[position + 1] = bill
            }
        }
        selectedItems.putAll(updated)
    }
}
