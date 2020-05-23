require 'sinatra'
require './lib/checksum_generator.rb'

class ChecksumTestApp < Sinatra::Application

  get '/' do
    erb :index
  end

  post '/generate-checksum' do
    if params[:checksum_text]
      @checksum =  ChecksumGenerator.new.call(params[:checksum_text])
    end
    erb :checksum
  end

end