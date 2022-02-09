package com.example.emptyproject.lesson04_3

import java.util.ArrayDeque

fun main() {

    val departmentsFactory = DepartmentsFactory()
    val directorate = departmentsFactory.createDepartments()

    showDepartments(directorate)
    showYoungEmployees(directorate)
    showOldEmployees(directorate)
    println(getSalarySum(directorate))
}

fun showDepartments(directorate: Department) {
    val queue = ArrayDeque<Department>()
    queue.add(directorate)
    while (queue.isNotEmpty()) {
        val department = queue.remove()
        for (employee in department.employees) {
            println("${department.name} - ${employee.name}")
        }
        for (dep in department.units) {
            queue.add(dep)
        }
    }
}

fun showYoungEmployees(directorate: Department) {
    val queue = ArrayDeque<Department>()
    queue.add(directorate)
    while (queue.isNotEmpty()) {
        val department = queue.remove()
        for (employee in department.employees) {
            if (employee.age < 25) {
                println("${department.name} - ${employee.name}, возраст: ${employee.age} ")
            }
        }
        for (dep in department.units) {
            queue.add(dep)
        }
    }
}
fun showOldEmployees(directorate: Department) {
    val queue = ArrayDeque<Department>()
    queue.add(directorate)
    while (queue.isNotEmpty()) {
        val department = queue.remove()
        for (employee in department.employees) {
            if (employee.age >= 35) {
                println("${department.name} - ${employee.name}, возраст: ${employee.age}")
            }
        }
        for (dep in department.units) {
            queue.add(dep)
        }
    }
}

fun getSalarySum(directorate: Department): Int {
    var sum = 0
    val queue = ArrayDeque<Department>()
    queue.add(directorate)
    while (queue.isNotEmpty()) {
        val department = queue.remove()
        for (employee in department.employees) {
            sum += employee.salary
        }
        for (dep in department.units) {
            queue.add(dep)
        }
    }
    return sum
}