package category.domain

import category.data.CategoryItem
import category.data.CategoryList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CategoryInteractorTest {
    private val interactor = CategoryInteractor(
        object : CategoryGateway {
            override suspend fun request(): CategoryEntity {
                return CategoryEntity(
                    categoryList = CategoryList(
                        categories = listOf<CategoryItem>(
                            CategoryItem(
                                idCategory = null,
                                strCategory = "Vegetable",
                                strCategoryThumb = null,
                                strCategoryDescription = null,
                            ),
                            CategoryItem(
                                idCategory = null,
                                strCategory = "Vegan",
                                strCategoryThumb = null,
                                strCategoryDescription = null,
                            ),
                            CategoryItem(
                                idCategory = null,
                                strCategory = "Stake",
                                strCategoryThumb = null,
                                strCategoryDescription = null,
                            )
                        )
                    )
                )
            }

        }

    )

    var data : CategoryEntity? = null

    @BeforeEach
    fun setUp() {
        runBlocking {
            data = interactor.fetchData()
        }
    }

    @AfterEach
    fun tearDown() {
        data = null
    }

    @Test
    fun fetchData() {

    }

    @Test
    fun sortByName() {
        runBlocking {
            assertIterableEquals(
                listOf("Stake", "Vegan", "Vegetable"),
                interactor.sortByName(data).categoryList?.categories?.map { it.strCategory }

            )
        }
    }

    @Test
    fun sortDescendingByName() {
        runBlocking {
            assertIterableEquals(
                listOf("Vegetable", "Vegan", "Stake"),
                interactor.sortDescendingByName(data).categoryList?.categories?.map { it.strCategory },
                "sortDescendingByName failed"

            )
        }
    }

    @Test
    fun filterCategoryList() {
        runBlocking {
            assertIterableEquals(
                listOf( "Vegetable","Vegan", ),
                interactor.filterCategoryList("Veg", data).categoryList?.categories?.map { it.strCategory },
                "filter Veg failed"
            )
            assertIterableEquals(
                listOf( "Vegetable","Stake", ),
                interactor.filterCategoryList("ta", data).categoryList?.categories?.map { it.strCategory },
                "filter \"ta\" failed"
            )
        }
    }
}