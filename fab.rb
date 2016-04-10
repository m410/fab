# Documentation: https://github.com/Homebrew/homebrew/blob/master/share/doc/homebrew/Formula-Cookbook.md
#                http://www.rubydoc.info/github/Homebrew/homebrew/master/Formula
# PLEASE REMOVE ALL GENERATED COMMENTS BEFORE SUBMITTING YOUR PULL REQUEST!

class Fab < Formula
  desc "java centric build tool and framework"
  homepage "http://m410.org/fabricate"
  url "https://raw.githubusercontent.com/m410/fab/master/fab-0.1.tar.gz"
  version "0.1"
  sha256 "4b3931b432cb92eb067fbc342db7e4d4d2b7c70e78744ffdba274956b42f6212"

  bottle :unneeded

  depends_on :java

  def install
    libexec.install %w[bin lib]
    bin.install_symlink libexec+"bin/fab"
  end
  
  test do
    ENV.java_cache
    output = shell_output("#{bin}/fab --version")
    assert_match /Fabricate #{version}/, output
  end
end
