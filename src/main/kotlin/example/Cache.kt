package example

import io.micronaut.cache.annotation.Cacheable
import javax.inject.Singleton
import java.util.concurrent.CompletableFuture


interface CacheService {
    fun cacheIt(input: CacheMeIfYouCan): CompletableFuture<CacheMeIfYouCan>
}

@Singleton
open class Cache : CacheService {
    @Cacheable(cacheNames = ["example"])
    override fun cacheIt(input: CacheMeIfYouCan): CompletableFuture<CacheMeIfYouCan> {
      return nothing(input)
    }

    fun nothing(input: CacheMeIfYouCan): CompletableFuture<CacheMeIfYouCan> {
        return CompletableFuture.completedFuture(input)
    }
}

data class CacheMeIfYouCan(
        val simple: Simple,
        val complex: List<Simple>
)

data class Simple(
        val string: String
)