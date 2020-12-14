class SpecLetterCounter < ChecksumFilter
  @uppercase_vowels = 0
  @consonants = 0

  def initialize next_filter
    @next_filter = next_filter
  end

  def add_char(c)
    if c != ' '
      if UPPERCASEVOWELS.inArray c
        @uppercase_vowels += 1
      elsif LOWERCASEVOWELS.inArray c
        @consonants += 1
      end
    end
    @next_filter.addChar c
  end


  def close
    @next_filter.close
  end
end