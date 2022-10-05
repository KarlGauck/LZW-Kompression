fun main(args: Array<String>)
{
    println("Please input an input message")
    println("UTF-8 format is supported")
    var input = readLine()
    if (input != null)
    {
        compress(input)
    }
}

fun compress(input: String)
{
    // Create dictionary and store all unicode-characters into it
    val dictionary: MutableMap<UInt, String> = mutableMapOf<UInt, String>()
    var nextDictionaryKey: UInt = 0u
    fun putValue(value: String)
    {
        dictionary[nextDictionaryKey] = value
        nextDictionaryKey++
    }
    fun getKey(sequence: String): UInt = dictionary.keys.first { dictionary[it] == sequence }

    for (c in Char.MIN_VALUE..Char.MAX_VALUE)
        putValue(c.toString())

    // Make a copy of the input string, to be editable
    // Then compress the message using LZW-Compression
    var result = mutableListOf<UInt>()
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

    for (short in result)
    {
        var s = Integer.toBinaryString(short.toInt())
        for (i in 1..32-s.length)
            s = "0$s"
        println(s)
    }
    println(Integer.toBinaryString(-1))
}
