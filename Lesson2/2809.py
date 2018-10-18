import unittest

# Given a string and a non-negative int n, return a larger string
# that is n copies of the original string.
from collections import Counter


def string_times(string, n):
    return string * n


# Given an array of ints, return True if one of the first 4 elements
# in the array is a 9. The array length may be less than 4.
def array_front9(nums):
    boole = False
    for i, el in enumerate(nums):
        if (i <= 3) and (el == 9):
            boole = True
    return boole


# Given a string, return the count of the number of times
# that a substring length 2 appears  in the string and also as
# the last 2 chars of the string, so "hixxxhi" yields 1 (we won't count the end substring).
def last2(string):
    count=0
    sub = string[-2:]
    print(sub)
    for i in range(0, len(string)-3):
        substring=string[i:i+2]
        if substring==sub:
            count+=1

    return count


# Write a proramm that returna dictionary of occurences of the alphabet for a given string.
# Test it with the Lorem upsuj
# "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
def occurences(text):
    result = {}
    lower_text = text.lower()
    for character in lower_text:
        if character in result:
            result[character] += 1
        else:
            result[character] = 1
    return result


# Write a program that maps a list of words into a list of
# integers representing the lengths of the correponding words.
def length_words(array):
    return map(len, array)


# Write a function that takes a number and returns a list of its digits.
def number2digits(number):
    return [int(z) for z in str(number)]


# Write function that translates a text to Pig Latin and back.
# English is translated to Pig Latin by taking the first letter of every word,
# moving it to the end of the word and adding 'ay'
def pigLatin(text):
    text=text.lower()
    words=text.split(" ")
    str=""
    for word in words:
        word= word[1:]+word[0]+"ay"
        str+=word+" "

    str=str[0].upper()+str[1:-1]
    return str


# write fizbuzz programm
def fizbuzz():
    return


response = {
    "nhits": 1000,
    "parameters": {},
    "records": [
        {
            "datasetid": "les-1000-titres-les-plus-reserves-dans-les-bibliotheques-de-pret",
            "recordid": "4b950c1ac5459379633d74ed2ef7f1c7f5cc3a10",
            "fields": {
                "nombre_de_reservations": 1094,
                "url_de_la_fiche_de_l_oeuvre": "https://bibliotheques.paris.fr/Default/doc/SYRACUSE/1009613",
                "url_de_la_fiche_de_l_auteur": "https://bibliotheques.paris.fr/Default/doc/SYRACUSE/1009613",
                "support": "indéterminé",
                "auteur": "Enders, Giulia",
                "titre": "Le charme discret de l'intestin [Texte imprimé] : tout sur un organe mal aimé"
            },
            "record_timestamp": "2017-01-26T11:17:33+00:00"
        },
        {
            "datasetid": "les-1000-titres-les-plus-reserves-dans-les-bibliotheques-de-pret",
            "recordid": "3df76bd20ab5dc902d0c8e5219dbefe9319c5eef",
            "fields": {
                "nombre_de_reservations": 746,
                "url_de_la_fiche_de_l_oeuvre": "https://bibliotheques.paris.fr/Default/doc/SYRACUSE/1016593",
                "url_de_la_fiche_de_l_auteur": "https://bibliotheques.paris.fr/Default/doc/SYRACUSE/1016593",
                "support": "Bande dessinée pour adulte",
                "auteur": "Sattouf, Riad",
                "titre": "L'Arabe du futur [Texte imprimé]. 2. Une jeunesse au Moyen-Orient, 1984-1985"
            },
            "record_timestamp": "2017-01-26T11:17:33+00:00"
        },
    ]
}





class Lesson1Tests(unittest.TestCase):
    fizbuzz()

    def testArrayFront9(self):
        self.assertEqual(array_front9([1, 2, 9, 3, 4]), True)
        self.assertEqual(array_front9([1, 2, 3, 4, 9]), False)
        self.assertEqual(array_front9([1, 2, 3, 4, 5]), False)

    def testStringTimes(self):
        self.assertEqual(string_times('Hel', 2), 'HelHel')
        self.assertEqual(string_times('Toto', 1), 'Toto')
        self.assertEqual(string_times('P', 4), 'PPPP')

    def testLast2(self):
        self.assertEqual(last2('hixxhi'), 1)
        self.assertEqual(last2('xaxxaxaxx'), 1)
        self.assertEqual(last2('axxxaaxx'), 2)

    def testLengthWord(self):
        self.assertEqual(length_words(['hello', 'toto']), [5, 4])
        self.assertEqual(length_words(['s', 'ss', '59fk', 'flkj3']), [1, 2, 4, 5])

    def testNumber2Digits(self):
        self.assertEqual(number2digits(8849), [8, 8, 4, 9])
        self.assertEqual(number2digits(4985098), [4, 9, 8, 5, 0, 9, 8])

    def testPigLatin(self):
        self.assertEqual(pigLatin("The quick brown fox"), "Hetay uickqay rownbay oxfay")


def main():
    unittest.main()


if __name__ == '__main__':
    main()
