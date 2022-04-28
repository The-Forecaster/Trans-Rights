package trans.rights.client.impl.setting

import trans.rights.client.api.setting.Setting

class EnumSetting<T: Enum<*>>(name: String, description: String, default: T, isParentSetting: Boolean = false) : Setting<T>(name, description, default, isParentSetting) {
    private val values = default.javaClass.enumConstants

    fun cycle() {
        if (this.value == this.values[this.values.size - 1]) {
            this.value = this.values[0]
            return
        }
        this.value = this.values[this.values.indexOf(this.value) + 1]
    }

    /**
     * set function but accepts a string instead of an enum constant
     *
     * @param other string to set the enum value to
     * @return true if the string is equal to one of the enum constants in the enum's base class, false otherwise
     */
    fun set(other: String) : Boolean {
        for (value in this.values) {
            if (other.lowercase() == value.toString().lowercase()) {
                this.set(value)
                return true
            }
        }
        return false
    }
}
