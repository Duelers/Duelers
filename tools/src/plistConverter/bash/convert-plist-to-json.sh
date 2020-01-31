# plist to json convertor
# how to use: bash convert-plist-to-json /path/to/.plists initialJsons
cd $1
mkdir $2

for file in *.plist
do
    plist-to-json $file > "$2/$file.json"
    echo "$file converted"
done