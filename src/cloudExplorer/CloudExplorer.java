/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cloudExplorer;

public class CloudExplorer {

    public static void helpargs() {
        System.out.print("\n[Cloud Explorer - CLI arguments]\n"
                + "\nput filename bucket"
                + "\nget filename bucket destination"
                + "\ndelete filename bucket"
                + "\nls bucket"
                + "\nlistbuckets"
                + "\nrmbucket bucket"
                + "\nmakebucket what"
                + "\nsynctos3 location bucket [folder]"
                + "\nsyncfroms3 destination bucket [folder]"
                + "\nuploadtest bucket size threads operations [mixed]"
                + "\ndownloadtest bucket size threads operations [mixed]"
                + "\nsearch bucket what"
                + "\ncreatefolder bucket what"
                + "\ndeleteall bucket"
                + "\nupgrade"
                + "\n\n\n");
    }

    public static void main(String[] args) {
        int stop = 0;

        try {
            Thread UpdateThread = new Thread(new Update(null, true, false));
            UpdateThread.start();
            UpdateThread.join();
            stop = 1;
        } catch (Exception upgrade) {
        }

        if (args.length > 0) {
            if (args[0].contains("daemon")) {
                Daemon daemon = new Daemon();
                daemon.gui = false;
                daemon.start();
                stop = 1;
            } else if (args[0].contains("listbuckets")) {
                CLI cli = new CLI();
                cli.start(args[0], null, null, null, null, null);
                stop = 1;

            } else if (args[0].contains("upgrade")) {
                try {
                    Thread UpdateThread = new Thread(new Update(null, false, false));
                    UpdateThread.start();
                    UpdateThread.join();
                    stop = 1;
                } catch (Exception upgrade) {
                }
            }

            if (stop == 0) {
                if (args.length >= 2) {

                    if (args[0].contains("build")) {
                        Build build = new Build();
                        build.start(args[1], args[2], args[3]);
                    } else {
                        CLI cli = new CLI();
                        if (args.length < 3) {
                            cli.start(args[0], args[1], null, null, null, null);
                        }
                        if (args.length == 3) {
                            cli.start(args[0], args[1], args[2], null, null, null);
                        }
                        if (args.length == 4) {
                            cli.start(args[0], args[1], args[2], args[3], null, null);
                        }
                        if (args.length == 5) {
                            cli.start(args[0], args[1], args[2], args[3], args[4], null);
                        }
                        if (args.length == 6) {
                            cli.start(args[0], args[1], args[2], args[3], args[4], args[5]);
                        }
                    }

                } else {
                    System.out.print("\nError: not enough arguments used.\n\n\n");
                    helpargs();
                }
            }

        } else {
            try {
                NewJFrame gui = new NewJFrame();
                gui.main(args);
            } catch (Exception S3) {
            }
        }
    }
}
