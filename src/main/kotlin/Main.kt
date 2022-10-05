import kotlin.math.pow

fun main(args: Array<String>)
{
    println("Please input an input message")
    println("UTF-8 format is supported")
    var input = readLine()
    if (input != null)
    {
        decompress(compress(input))
    }
}

fun compress(input: String): MutableList<Int>
{
    // Create dictionary and store all unicode-characters into it
    val dictionary: MutableMap<Int, String> = mutableMapOf<Int, String>()
    var nextDictionaryKey: Int = 0
    fun putValue(value: String)
    {
        dictionary[nextDictionaryKey] = value
        nextDictionaryKey++
    }
    fun getKey(sequence: String): Int = dictionary.keys.first { dictionary[it] == sequence }

    for (c in Char.MIN_VALUE..Char.MAX_VALUE)
        putValue(c.toString())

    // Make a copy of the input string, to be editable
    // Then compress the message using LZW-Compression
    var result = mutableListOf<Int>()
    var workingString = input

    // Do the following while the message is not fully compressed
    while (workingString != "")
    {
        // Iterate through left message char by char until a currently unknown sequence is found
        var currentSequence = workingString[0].toString()
        println("message is \"$workingString\"\n")
        if (workingString.length == 1)
        {
            result.add(getKey(workingString))
            println("last key saved as \"${getKey(workingString)}\"")
            break
        }

        for (currentCharIndex in 1..Int.MAX_VALUE)
        {
            // If the next sequence is known, continue iteration
            val nextPossibleSequence = currentSequence + workingString[currentCharIndex]
            println("nextChar = ${workingString[currentCharIndex]}")
            if (nextPossibleSequence in dictionary.values && currentSequence.length+1 != workingString.length)
            {
                currentSequence = nextPossibleSequence
                println("\"$currentSequence\" is known")
            }
            // If the next sequence is not known or its the last sequence, save the current sequence and add the next sequence into the dictionary
            else
            {
                result.add(getKey(currentSequence))
                putValue(nextPossibleSequence)
                println("\"$nextPossibleSequence\" is not known -> saved as \"${getKey(nextPossibleSequence)}\"")
                println("encode \"$currentSequence\" as \"${getKey(currentSequence)}")
                workingString = workingString.replaceFirst(currentSequence, "")
                break
            }
        }
    }

    /*
    for (short in result)
    {
        var s = toBinary(short, 16)
        println("$s base 2 -> ${fromBinaryToInt(s)} base 10 -> ${toBinary(fromBinaryToInt(s), 16)}")
    } */
    return result
}

fun decompress(input: MutableList<Int>): String
{
    // Create dictionary and store all unicode-characters into it
    val dictionary: MutableMap<Int, String> = mutableMapOf<Int, String>()
    var nextDictionaryKey: Int = 0
    fun putValue(value: String)
    {
        dictionary[nextDictionaryKey] = value
        nextDictionaryKey++
    }
    fun getKey(sequence: String): Int = dictionary.keys.first { dictionary[it] == sequence }

    for (c in Char.MIN_VALUE..Char.MAX_VALUE)
        putValue(c.toString())


    var message = dictionary[input[0]]
    for (i in 1..input.size-1)
    {
        var r = dictionary[input[i]]
        message += dictionary[input[i]]
        putValue(dictionary[input[i-1]] + dictionary[input[i]]?.get(0))
    }
    print(message)
    return message!!
}

fun toBinary(x: Int, len: Int): String
{
    return String.format(
        "%" + len + "s",
        Integer.toBinaryString(x)
    ).replace(" ".toRegex(), "0")
}

fun fromBinaryToInt(binary: String): Int {
    var num = 0
    var i = 0
    for (c in binary.reversed())
    {
        if (c == '1')
            num += 2f.pow(i).toInt()
        i++
    }
    return num
}