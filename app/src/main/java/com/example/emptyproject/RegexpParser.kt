package com.example.emptyproject

import java.util.regex.Pattern

class RegexpParser {

    fun transformString(str: String, regexpMode: String): String {
        return when (regexpMode) {
            RegexpMode.SPACES_TO_HYPHENS -> replaceSpacesWithHyphens(str)
            RegexpMode.CHANGE_PHONE_NUMBER -> changePhoneNumberFormat(str)
            RegexpMode.CHANGE_WORD -> uppercaseFourLetterWord(str)
            RegexpMode.FIND_TEXT_INSIDE_TAGS -> findTextInsideTags(str)
            RegexpMode.ADD_HTTP_TO_LINK -> addHttpToLinks(str)
            else -> str
        }
    }

    private fun replaceSpacesWithHyphens(str: String): String {
        val result = str.replace("""\s+""".toRegex(), "-")
        return result
    }

    private fun changePhoneNumberFormat(str: String): String {
        val regexp = """8\s\(0(\d{2})\)\s(\d{3}-\d{2}-\d{2})"""
        val pattern: Pattern = Pattern.compile(regexp)
        val matcher = pattern.matcher(str)
        val result = buildString {
            while (matcher.find()) {
                append(
                    matcher.group().toString().replace(
                        regexp.toRegex(),
                        "+375-\$1-\$2"
                    )
                )
                append("\n")
            }
        }
        return result
    }

    private fun uppercaseFourLetterWord(str: String): String {
        val regexp = """\b([А-Яа-я]|[A-Za-z]){4}\b"""
        val result = str.replace(regexp.toRegex()) { m -> m.groupValues[0].uppercase() }

        return result
    }

    private fun findTextInsideTags(str: String): String {
        val regex = Regex("""<one>([^<>]*)</one>""")
        val matches = regex.findAll(str)
        val result = matches.map { it.groupValues[1] }.joinToString(separator = "\n")

        return result
    }

    private fun addHttpToLinks(str: String): String {
        val regexp = """(\swww|^www)\.(((\w|\-)+\.)+(\w|\-)+\s)"""
        val result = str.replace(regexp.toRegex(), " http://www\\.\$2")

        return result
    }

}