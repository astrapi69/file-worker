# How file-worker is made

file-worker was written by its maintainer, Asterios Raptis, over many years: its history in this
repository starts in November 2015, and up to release 19.0 (November 2024) no commit names an AI
tool.

Since 2026 he uses AI agents as a tool to maintain and modernize his projects
([How my projects are made](https://github.com/astrapi69/astrapi69/blob/main/AI_USAGE.md)). In
file-worker the history first shows it in five commits of 2026-08-19, between `RELEASE-19.0` and
`RELEASE-19.1`, that name an agent as co-author: the Gradle wrapper 9.7.0, the move of the
deployment to the Central Portal, the versions of the dependencies, the plugins and the CI actions,
and a test fixture that had not been committed. They are 5 of the 54 commits of that range; none of
its 3 pull requests says it was generated with an agent.

For 20.0 the history shows no such line: none of the 74 commits between `RELEASE-19.1` and
`RELEASE-20.0` names a co-author, and none of the 28 pull requests merged in that range carries a
"Generated with Claude Code" line. These counts do not tell how much of 20.0 an agent wrote: the
maintainer's page says that since November 2026 commits and pull requests carry no AI attribution
line.

What the history shows of the maintainer's part: every pull request in this repository was merged
from his account, every release on Maven Central is signed with his key, by its signing subkey
since 19.1 (the README's "Verifying the release signatures" says how to check), and a release
waits in the Central Portal until he publishes it there (`publishingType = "USER_MANAGED"` in
`settings.gradle`).

As his page says, the agents are tools, not authors. How he uses them across his projects, and what
he decides: [How my projects are made](https://github.com/astrapi69/astrapi69/blob/main/AI_USAGE.md).
