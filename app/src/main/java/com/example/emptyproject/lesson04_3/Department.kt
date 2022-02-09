package com.example.emptyproject.lesson04_3

class Department(val name: String, val employees: List<Employee>, val units: List<Department> = listOf()) {

}