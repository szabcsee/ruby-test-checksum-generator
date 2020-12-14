class WordCounterFilter < ChecksumFilter
  @count = 0
  @is_space = true

  def initialize next_filter
    @next_filter = next_filter
  end

  def add_char char
    if @is_space
      if char != ' '
        @word_count += 1
        @is_space = false
      end
    else
      @is_space = char == ' '

    end
    @next_filter.add_char char
  end

  def close
    @next_filter.close
  end
end