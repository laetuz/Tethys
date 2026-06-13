package id.neotica.tethys.utils

fun Char.mirrorBraille(): Char {
    if (this !in '\u2800'..'\u28FF') return this
    val base = this.code - 0x2800
    var mirrored = 0
    if (base and 0x01 != 0) mirrored = mirrored or 0x08
    if (base and 0x08 != 0) mirrored = mirrored or 0x01
    if (base and 0x02 != 0) mirrored = mirrored or 0x10
    if (base and 0x10 != 0) mirrored = mirrored or 0x02
    if (base and 0x04 != 0) mirrored = mirrored or 0x20
    if (base and 0x20 != 0) mirrored = mirrored or 0x04
    if (base and 0x40 != 0) mirrored = mirrored or 0x80
    if (base and 0x80 != 0) mirrored = mirrored or 0x40
    return (0x2800 + mirrored).toChar()
}

fun String.mirror(): String {
    val normalized = this.replace(' ', '⠀')
    val lines = normalized.lines()
    val maxLength = lines.maxOfOrNull { it.length } ?: 0
    return lines.joinToString("\n") { line ->
        line.padEnd(maxLength, '⠀')
            .reversed()
            .map { it.mirrorBraille() }
            .joinToString("")
    }
}
