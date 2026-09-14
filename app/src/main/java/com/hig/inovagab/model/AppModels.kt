package com.hig.inovagab.model

enum class UserRole{ OPERATOR, MANAGER, LEADER }
enum class IdeaStatus{PENDING, APPROVED, REJECTED}
enum class ProjectStage{PLANNING, EXECUTION, COMPLETED}
enum class ProjectStatus {ACTIVE, ON_HOLD, CANCELLED}


data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val user: User)

data class User(
    val id: String?,
    val name: String,
    val email: String,
    val role: UserRole
)

data class Strategy(
    val id: String?,
    val title: String,
    val category: String,
    val campaign: String,
    val date: String,
    val description: String
)

data class Idea(
    val id: String?,
    val title: String,
    val description: String,
    val status: IdeaStatus,
    val authorId: String,
    val strategyId: String? = null
)

data class Project(
    val id: String?,
    val title: String,
    val stage: ProjectStage,
    val status: ProjectStatus,
    val investment: Double,
    val deadline: String,
    val financialReturn: Double,
    val description: String,
    val strategyId: String? = null,
    val results: String? = null
)

