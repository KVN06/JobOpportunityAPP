package com.kvn.jobopportunityapp.data

// Tipos de usuario en la aplicación
enum class UserType {
    CESANTE,    // Persona desempleada buscando trabajo
    EMPRESA     // Empresa ofreciendo empleos
}

// Permisos para publicar contenido según tipo de usuario
data class PublishPermissions(
    val canPublishJobs: Boolean = false,
    val canPublishClassifieds: Boolean = false,
    val canPublishTraining: Boolean = false
)

// Función para obtener permisos según tipo de usuario
fun UserType.getPublishPermissions(): PublishPermissions {
    return when (this) {
        UserType.CESANTE -> PublishPermissions(
            canPublishJobs = false,        // Los cesantes no publican empleos
            canPublishClassifieds = true,  // Pueden vender/intercambiar cosas
            canPublishTraining = true      // Pueden ofrecer capacitaciones/tutorías
        )
        UserType.EMPRESA -> PublishPermissions(
            canPublishJobs = true,         // Las empresas publican empleos
            canPublishClassifieds = true,  // Pueden vender equipos/servicios
            canPublishTraining = true      // Pueden ofrecer cursos corporativos
        )
    }
}

// Opciones de publicación para mostrar en el menú
data class PublishOption(
    val title: String,
    val description: String,
    val route: String
)

// Función para obtener opciones de publicación según permisos
fun PublishPermissions.getAvailableOptions(): List<PublishOption> {
    val options = mutableListOf<PublishOption>()
    
    if (canPublishJobs) {
        options.add(
            PublishOption(
                title = "Publicar Empleo",
                description = "Crear una nueva oferta laboral",
                route = "publish_job"
            )
        )
    }
    
    if (canPublishClassifieds) {
        options.add(
            PublishOption(
                title = "Publicar Clasificado",
                description = "Vender, comprar o intercambiar",
                route = "publish_classified"
            )
        )
    }
    
    if (canPublishTraining) {
        options.add(
            PublishOption(
                title = "Publicar Capacitación",
                description = "Ofrecer curso o entrenamiento",
                route = "publish_training"
            )
        )
    }
    
    return options
}
