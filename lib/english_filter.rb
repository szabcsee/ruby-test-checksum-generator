class EnglishFilter < ChecksumFilter
  @count = 0
  @is_space = true

  def initialize next_filter
    @next_filter = next_filter
  end

  # @param [string] char
  def add_char char
    if ('A' <= char && char <= 'Z') || ('a' <= char && char <= 'z') || char == ' '
        @nextFilter.add_char char
    end
  end

  def close
    @next_filter.close
  end
end