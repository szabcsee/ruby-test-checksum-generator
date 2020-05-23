require 'sinatra'
require 'json'

class ChecksumTestApp < Sinatra::Application
  set :public_folder, Proc.new { File.join(root, "client") }

  get '/' do
    File.read(File.join('client', 'index.html'))
  end

end