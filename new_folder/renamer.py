# For renaming episodes' files to the format S01E01 "Episode Name".mp4

import os

# This is the one you're gonna wanna use. Run the file then call
# rename_episodes() from the shell. If on a Mac, full path will
# look similar to
# /Users/callahanhirrel/Desktop/Rick and Morty/Season 1

# NOTE: If you are prompted with a file you do not wish to rename,
# then just press enter until you are prompted with the next
# file.
def rename_episodes():
    
    path = input("Full path to season folder: ")
    episodes = os.listdir(path)
    
    for ep in episodes:
        print("\n" + ep + "\n")
        season_num = input("Season: ")
        ep_num = input("Episode: ")
        ep_name = input("Episode name: ")
        extension = ep[-4:]

        if ep_name == "":
            new_name = ep
        else:
            new_name = 'S' + season_num + 'E' + ep_num + ' "' + \
                        ep_name + '"' + extension
            
        os.rename(path + "/" + ep, path + "/" + new_name)

# Obvious test is obvious
def test():
    path = "/Users/callahanhirrel/Desktop/"
    desktop = os.listdir(path)
    for thing in desktop:
        if ".rtf" in thing:
            name = thing[:thing.index("_")] + ".rtf"
            os.rename(path + "/" + thing, path + "/" + name)

# Only used for season 3 of Steven Universe
def steven_3():
    path = "/Users/callahanhirrel/Desktop/Current Shows/Season 3/"
    episodes = os.listdir(path)
    for ep in episodes:
        name = ep[16:ep.index("-")] + '"' + \
               ep[ep.index("-") + 2:ep.index(".mp4")] + '".mp4'
        os.rename(path + "/" + ep, path + "/" + name)
