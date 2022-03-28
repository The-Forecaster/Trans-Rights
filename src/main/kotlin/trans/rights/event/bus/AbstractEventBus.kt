package trans.rights.event.bus

import java.util.Collections
import java.util.Arrays
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import trans.rights.event.listener.Listener
import trans.rights.event.listener.impl.LambdaListener
import trans.rights.event.bus.ListenerType.*

abstract class AbstractEventBus(val type: ListenerType) : EventBus {
    val registry: ConcurrentHashMap<Class<*>, MutableSet<Listener<*>>> = ConcurrentHashMap()

    private val subscribers: MutableSet<Any> = Collections.synchronizedSet(mutableSetOf())

    private val cache: MutableMap<Class<*>, MutableSet<Listener<*>>> = ConcurrentHashMap()

    constructor() : this(ListenerType.BOTH)

    /** Finds and registers all valid listener fields in a target object class */
    abstract fun registerFields(subscriber: Any)

    /** Finds and registers all valid methods in a target object class */
    abstract fun registerMethods(subscriber: Any)

    /** Finds and removes all valid fields from the subscriber registry */
    abstract fun unregisterFields(subscriber: Any)

    /** Finds and removes all valid methods from the subscriber registry */
    abstract fun unregisterMethods(subscriber: Any)

    override fun register(subscriber: Any) {
        if (isRegistered(subscriber)) return

        when(this.type) {
            LAMBDA -> this.registerFields(subscriber)
            METHOD -> this.registerMethods(subscriber)
            else -> {
                this.registerFields(subscriber)
                this.registerMethods(subscriber)
            }
        }

        this.subscribers.add(subscriber)
    }

    fun register(vararg listeners: Listener<*>) {
        listeners.asList().stream().forEach { listener ->
            this.registry.getOrPut(listener.target, ::CopyOnWriteArraySet).add(listener)
        }

        this.registry.values.stream().forEach { set ->
            set.stream().sorted((Comparator.comparingInt(Listener<*>::priority)))
        }
    }

    override fun unregister(subscriber: Any) {
        if (!isRegistered(subscriber)) return

        when(this.type) {
            LAMBDA -> this.unregisterFields(subscriber)
            METHOD -> this.unregisterMethods(subscriber)
            else -> {
                this.registerFields(subscriber)
                this.registerMethods(subscriber)
            }
        }

        this.subscribers.remove(subscriber)
    }

    override fun isRegistered(subscriber: Any): Boolean {
        return this.subscribers.contains(subscriber)
    }

    override fun <T : Any> dispatch(event: T): T {
        if (this.registry[event::class.java]?.size != 0) {
            this.getOrPutList(event.javaClass).stream().forEach { listener -> listener.invoke(event) }
        }

        return event
    }

    protected fun <T : Any> getOrPutList(clazz: Class<T>): CopyOnWriteArraySet<Listener<T>> {
        return this.registry.getOrPut(clazz, ::CopyOnWriteArraySet) as CopyOnWriteArraySet<Listener<T>>
    }
}

enum ListenerType {
    METHOD,
    LAMBDA,
    BOTH
}
