# Documentation: https://github.com/Homebrew/homebrew/blob/master/share/doc/homebrew/Formula-Cookbook.md
#                http://www.rubydoc.info/github/Homebrew/homebrew/master/Formula
# PLEASE REMOVE ALL GENERATED COMMENTS BEFORE SUBMITTING YOUR PULL REQUEST!

class Fab < Formula
  desc "java centric build tool and framework"
  homepage "http://m410.org/fabricate"
  url "https://github.com/m410/fab/releases/download/0.2/fab-0.3.tar.gz"
  version "0.2"
  sha256 "6545382a4f7a7e9840ddc2a549cb189f8962353286a7838520f8ae1cd98b06aa"

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
