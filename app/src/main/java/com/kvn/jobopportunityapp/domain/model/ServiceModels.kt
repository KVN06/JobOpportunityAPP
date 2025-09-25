package com.kvn.jobopportunityapp.domain.model

data class ServiceProvider(
    val name: String,
    val specialization: String,
    val rating: Float,
    val reviews: Int,
    val price: String
)

fun getServiceProviders(category: String): List<ServiceProvider> {
    return when (category) {
        "Desarrollo Web" -> listOf(
            ServiceProvider("Juan Carlos M.", "Frontend & Backend", 4.8f, 25, "$500.000"),
            ServiceProvider("María Elena R.", "React & Node.js", 4.9f, 18, "$650.000"),
            ServiceProvider("Carlos Andrés V.", "Full Stack Developer", 4.7f, 32, "$450.000"),
            ServiceProvider("Laura Sofía P.", "WordPress & Shopify", 4.8f, 21, "$400.000")
        )
        "Diseño Gráfico" -> listOf(
            ServiceProvider("Ana Isabel C.", "Branding & Logos", 4.9f, 45, "$200.000"),
            ServiceProvider("Roberto Díaz", "Diseño Digital", 4.7f, 28, "$250.000"),
            ServiceProvider("Carmen Torres", "Ilustración", 4.8f, 35, "$180.000"),
            ServiceProvider("Diego Muñoz", "Packaging Design", 4.6f, 22, "$220.000")
        )
        "Marketing Digital" -> listOf(
            ServiceProvider("Alejandro R.", "SEO & SEM", 4.8f, 41, "$350.000"),
            ServiceProvider("Valentina S.", "Social Media", 4.9f, 38, "$300.000"),
            ServiceProvider("Andrés López", "Google Ads", 4.7f, 29, "$400.000"),
            ServiceProvider("Camila Rojas", "Content Marketing", 4.8f, 33, "$280.000")
        )
        else -> listOf(
            ServiceProvider("Profesional 1", "Especialista", 4.5f, 15, "$150.000"),
            ServiceProvider("Profesional 2", "Experto", 4.7f, 20, "$200.000"),
            ServiceProvider("Profesional 3", "Consultor", 4.6f, 18, "$180.000")
        )
    }
}
