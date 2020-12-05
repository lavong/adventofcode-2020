class AdventOfCode {
    companion object {
        fun file(filePath: String): String {
            return AdventOfCode::class.java.classLoader.getResource(filePath).readText()
        }
    }
}
