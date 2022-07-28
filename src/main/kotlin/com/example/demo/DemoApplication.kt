package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

abstract class ApiBaseResult {
    val statusCode: String? = null
    val errorCode: String? = null
    val errorMessage: String? = null
}

sealed class QueryPostByIdResult : ApiBaseResult() {
    data class Success(
        val post: Post
    ) : QueryPostByIdResult()

    data class Error(
        val errors: List<String>? = emptyList()
    ) : QueryPostByIdResult()
}

@Controller
@RequestMapping("/posts")
class PostController(val posts: PostClientRepository) {

    @GetMapping("/{id}")
    suspend fun getPostById(id: Int) : ResponseEntity<Any>{
        return when(val result = posts.findById(id)) {
            is QueryPostByIdResult.Success -> ok( result.post )
            is QueryPostByIdResult.Error -> badRequest().build()
        }
    }
}

@Component
class PostClientRepository {
    suspend fun findById(id: Int): QueryPostByIdResult =
        when (id) {
            1 -> QueryPostByIdResult.Success(Post(id = 1, title = "title1", content = "content1"))
            else -> QueryPostByIdResult.Error(listOf("Post was not found"))
        }
}

data class Post(
    val id: Int? = null,
    var title: String? = null,
    var content: String? = null,
)