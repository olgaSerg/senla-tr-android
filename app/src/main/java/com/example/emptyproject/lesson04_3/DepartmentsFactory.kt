package com.example.emptyproject.lesson04_3

class DepartmentsFactory {
    fun createDepartments(): Department {

        val softwareDevelopment = Department(
            "Разработка ПО",
            listOf(
                Employee("Иван Петров", 26, 5000),
                Employee("Александр Долгорукий", 23, 3000),
            )
        )
        val mobileDevelopment = Department(
            "Мобильная разработка",
            listOf(
                Employee("Дмитрий Водянов", 27, 2500)
            )
        )
        val developmentManagement = Department(
            "Управление разработкой",
            listOf(
                Employee("Евгений Фотник", 32, 6000)
            ),
            listOf(softwareDevelopment, mobileDevelopment)
        )
        val functionalTesting = Department(
            "Фукциональное тестирование",
            listOf(
                Employee("Андрей Губин", 24, 2000)
            )
        )
        val testingQA = Department(
            "Тестирование качества",
            listOf(
                Employee("Сергей Зверев", 24, 2000),
                Employee("Наталья Уткина", 22, 1800)
            )
        )
        val testingManagement = Department(
            "Управление тестированием",
            listOf(
                Employee("Екатерина Мозайкина", 33, 6000)
            ),
            listOf(functionalTesting, testingQA)
        )
        val managementDepartment = Department(
            "Управление",
            listOf(
                Employee("Николай Сотейкин", 35, 7000),
                Employee("Иван Велесов", 33, 6500)
            ),
            listOf(developmentManagement, testingManagement)
        )
        val accountants = Department(
            "Бухгалтерия",
            listOf(
                Employee("Ольга Малышева", 37, 1500),
                Employee("Петр Зайцев", 36, 1400)
            )
        )
        val directorate = Department(
            "Директорат",
            listOf(
                Employee("Василий Иванов", 40, 8000)
            ),
            listOf(accountants, managementDepartment)
        )
        return directorate
    }
}