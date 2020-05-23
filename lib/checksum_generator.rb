class ChecksumGenerator
  def call text
    text = text.gsub(/[^a-z ]/i, '')
  end
end