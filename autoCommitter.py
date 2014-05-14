"""
A simple script that commits ALL changes to a repository

Copyright © 2014 Dimitri Molenaars <tyrope@tyrope.nl>
This work is free. You can redistribute it and/or modify it under the
terms of the Do What The Fuck You Want To Public License, Version 2,
as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
"""
from __future__ import print_function

conf = {}
#----------------------
#- User Configuration -
#----------------------
# Verbosity settings
conf['print_stdout'] = True # Print git output to console.
conf['print_stderr'] = True # Print git errors to console.
conf['silent'] = False # Silence non-git output.

# Push settings
conf['push'] = True # Whether or not to git push.
                    # If False, this program will only commit.
conf['remote'] = 'origin' # Remote repository you want to push to.
conf['branch'] = 'master' # Remote branch you want to push to.
#------------------------
#- End of Configuration -
#------------------------

from time import sleep
from datetime import datetime
import subprocess

#Prepare commands & Counter value
gitadd = ['git', 'add', '.']
gitcommit = ['git', 'commit', '-a', '--allow-empty', '-m']
gitpush = ['git', 'push', conf['remote'], conf['branch']]
counter = 1

#Helper method.
def gitcmd(cmd):
        # Open up a proccess
        p = subprocess.Popen(cmd, stdout = subprocess.PIPE,
            stderr = subprocess.PIPE)

        # Talk to me.
        out, err = p.communicate()

        # Thank you.
        if err != '' and conf['print_stderr']:
            print(err)
        if conf['print_stdout']:
            print(out)
        return

# Main Cycle start
if not conf['silent']:
    print("Starting the auto committer in 3, 2, 1, GO!")

while True:
    counter -= 1
    if counter > 0:
        # Wait...
        if not conf['silent']:
            print("Pushing to remote in %s minutes." % (counter+1,))
    else:
        # Add
        gitcmd(gitadd)

        # Commit
        if not conf['silent']:
            print("Committing")
        gitcmd(gitcommit + ['AutoCommit: '+str(datetime.now())])

        # Push
        if conf['push']:
            if not conf['silent']:
                print("Pushing.")
            gitcmd(gitpush)

        if not conf['silent']:
            print("Push complete. Starting new 15 minutes timer.")
        counter = 14

    sleep(60)

