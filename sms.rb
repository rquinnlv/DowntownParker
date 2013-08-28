require 'sinatra'
require 'json'
require 'pp'

set :bind,  '0.0.0.0'

get '/hi' do
    "Hello World!"
end

def read_file
    file = File.read("garages.json")
end

def parse_file(file)
  garages = JSON.parse(file)
  string = ""
  garages.each do |key, value|
    string += "#{key}: #{garages[key]["open_spots"]} spots.  "
  end
  return string
end


post '/garages' do
  begin
    file = read_file()
    string = parse_file(file)
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<Response>
    <Sms>#{string}</Sms>
</Response>"
  rescue => err
  ### We should send an email
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<Response>
    <Sms>We had an error.  Please contact Danger Mouse or Penfold immediately to resolve! #{err}</Sms>
</Response>"
    err
  end
end

post '/garagesText' do
  begin
    file = read_file()
    string = parse_file(file)
puts string
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<Response>
    <Say>#{string}</Say>
</Response>"
  rescue => err
  ### We should send an email
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<Response>
    <Sms>We had an error.  Please contact Danger Mouse or Penfold immediately to resolve! #{err}</Sms>
</Response>"
    err
  end
end

get '/getJson' do
  file = read_file()
  "#{file}"
end