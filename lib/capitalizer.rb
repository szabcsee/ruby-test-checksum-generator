class Capitalizer < ChecksumFilter

  @is_space = true

  def initialize next_filter
    @next_filter = next_filter
  end

  def add_char char
    if char == ' '
      @is_space = true
      @next_filter.add_char ' '
    else
      @next_filter.add_char @is_space ? char.upcase : char.downcase
      @is_space = false
    end
  end

  def close
    @next_filter.close
  end

end