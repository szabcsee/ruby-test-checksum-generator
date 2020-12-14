class Jonier < ChecksumFilter

  @array_of_spaces = Array.new.fill(' ', 0..9)

  @index = 0

  def initialize next_filter
    @next_filter = next_filter
  end

  def add_char char
    if @index == @array_of_spaces.length
      for char in 0..@array_of_spaces.length do
        @nextFilter.add_char char
      end
      @nextFilter.add_char ' '
      @index = 0
    end
    if char != ' '
      @array_of_spaces[@index] = char
      @index += 1
    end
  end

  def close
    for i in 0..index do
      @nextFilter.add_char @array_of_spaces[i]
    end
    @nextFilter.close
  end
end