package example

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.matchers.string.shouldMatch
import io.micronaut.context.ApplicationContext
import io.micronaut.test.annotation.MicronautTest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject


@MicronautTest
class CacheTest {

    @Inject
    lateinit var cache: Cache

    @Inject
    lateinit var applicationContext: ApplicationContext

    @Test
    fun runTest() {
        val simple = Simple(UUID.randomUUID().toString())
        val cacheInput = CacheMeIfYouCan(
                simple,
                emptyList()
        )

        val objectMapper = applicationContext.getBean(ObjectMapper::class.java)
        println(objectMapper.writeValueAsString(cacheInput))

        val output = cache.cacheIt(cacheInput)
        output.get().simple.string.shouldMatch(cacheInput.simple.string)

        val output2 = cache.cacheIt(cacheInput)
        val output2Flow = Single.fromFuture(output2).subscribeOn(Schedulers.io()).timeout(3,TimeUnit.SECONDS)
        try {
            output2Flow.blockingGet()
        } catch (e:TimeoutException) {
            return
        }
        throw Exception("Shouldn't enter this block")
    }
}