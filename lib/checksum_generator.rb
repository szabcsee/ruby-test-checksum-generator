class ChecksumGenerator
  VOWELS = ['A','E','O','U','I','a','e','o','u','i']

  def call text
    original_words = text.gsub(/[^a-z ]/i, '').split(/\W+/)
    @first_digit = original_words.length
    string = original_words.join('').split('')
    string.each_with_index do |value, index|
      add_char value, index
    end
  end

  def add_char value, index

  end

  def is_vowel char
    VOWELS.inArray(char)
  end
end