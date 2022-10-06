import java.io.File
import kotlin.math.pow

fun main(args: Array<String>)
{
    var compressedData: MutableList<Int>
    var uncompressedData: String

    /*
    if ("uncompress" in args)
    {
        if ("-r" in args)
        {
            val argInd = args.indexOf("-r") + 1
            var compressedString = File(args[argInd]).readLines().fold("") {R, it -> R + it}
            var compressedData = mutableListOf<Int>()

        }
    }
    else if ("compress" in args)
    {
        if ("-r" in args)
        {
            val argInd = args.indexOf("-r") + 1
            uncompressedData = File(args[argInd]).readLines().fold("") {R, it -> R + it}
            compressedData = compress(uncompressedData)
        }
    }
     */
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
        //println("message is \"$workingString\"\n")
        if (workingString.length == 1)
        {
            result.add(getKey(workingString))
            //println("last key saved as \"${getKey(workingString)}\"")
            break
        }

        for (currentCharIndex in 1..Int.MAX_VALUE)
        {
            // If the next sequence is known, continue iteration
            val nextPossibleSequence = currentSequence + workingString[currentCharIndex]
            //println("nextChar = ${workingString[currentCharIndex]}")
            if (nextPossibleSequence in dictionary.values && currentSequence.length+1 != workingString.length)
            {
                currentSequence = nextPossibleSequence
                //println("\"$currentSequence\" is known")
            }
            // If the next sequence is not known or its the last sequence, save the current sequence and add the next sequence into the dictionary
            else
            {
                result.add(getKey(currentSequence))
                putValue(nextPossibleSequence)
                //println("\"$nextPossibleSequence\" is not known -> saved as \"${getKey(nextPossibleSequence)}\"")
                //println("encode \"$currentSequence\" as \"${getKey(currentSequence)}")
                workingString = workingString.replaceFirst(currentSequence, "")
                break
            }
        }
    }

    for (short in result)
    {
        var s = toBinary(short, 16)
        println("$short -> ${dictionary[short]}")
    }
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

    for (c in Char.MIN_VALUE..Char.MAX_VALUE)
    {
        putValue(c.toString())
    }


    var message = dictionary[input[0]]
    for (i in 0 until input.size-1)
    {
        var j = if (input[i+1] in dictionary.keys) input[i+1] else input[i]
        putValue(dictionary[input[i]] + dictionary[j]?.get(0))
        message += dictionary[input[i+1]]
    }
    println("Decompressed message: \n $message")
    return message!!
}

fun dicMaxLen(dic: MutableMap<Int, String>, input: String, pos: Int)
{
    var maxLenWord = dic
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