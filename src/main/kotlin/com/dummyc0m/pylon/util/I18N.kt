package com.dummyc0m.pylon.util

import com.google.common.base.Splitter
import com.google.common.collect.Iterables
import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummyc0m on 3/9/16.
 * From Minecraft Class StringTranslate
 */
class I18N {
    private val languageList: MutableMap<String, String> = ConcurrentHashMap<String, String>()

    fun addLangFile(langFile: File) {
        try {
            val reader = BufferedReader(FileReader(langFile))
            addLangReader(reader)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun addLangResource(domain: String, lang: String) {
        val langStream = I18N::class.java.getResourceAsStream("/$domain/$lang.lang")
        if (langStream != null) {
            val reader = BufferedReader(InputStreamReader(langStream, Charset.forName("UTF-8")))
            addLangReader(reader)
        }
    }

    fun addLangStream(langStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(langStream, Charset.forName("UTF-8")))
        addLangReader(reader)
    }

    private fun addLangReader(reader: BufferedReader) {
        try {
            var line: String?
            while (true) {
                line = reader.readLine()
                if (line == null) {
                    break
                }
                if (!line.isEmpty() && line[0].toInt() != 35) {
                    val keyValPair = Iterables.toArray(equalSignSplitter.split(line), String::class.java)

                    if (keyValPair != null && keyValPair.size == 2) {
                        //val value = numericVariablePattern.matcher(keyValPair[1]).replaceAll("%$1s")
                        this.languageList.put(keyValPair[0], keyValPair[1])//value)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * Translate a key to current language.
     */
    fun translateKey(key: String): String {
        return this.tryTranslateKey(key)
    }

    /**
     * Translate a key to current language applying String.format()
     */
    fun translateKeyFormat(key: String, vararg varargs: Any): String {
        val translation = tryTranslateKey(key)

        try {
            return String.format(translation, *varargs)
        } catch (var5: IllegalFormatException) {
            return "Format error: " + translation
        }

    }

    /**
     * Tries to look up a translation for the given key; spits back the key if no result was found.
     */
    private fun tryTranslateKey(key: String): String {
        val translation = this.languageList[key]
        return translation ?: key
    }

    /**
     * Returns true if the passed key is in the translation table.
     */
    fun isKeyTranslated(key: String): Boolean {
        return this.languageList.containsKey(key)
    }

    companion object {
        /**
         * Pattern that matches numeric variable placeholders in a resource string, such as "%d", "%3$d", "%.2f"
         */
        //        private val numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]")

        /**
         * A Splitter that splits a string on the first "=".  For example, "a=b=c" would split into ["a", "b=c"].
         */
        private val equalSignSplitter = Splitter.on('=').limit(2)
    }
}
