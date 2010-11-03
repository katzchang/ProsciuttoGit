Prosciutto Git
=============

>「コミットする」…そんな言葉は使う必要がねーんだ。  
>なぜなら、オレや、オレたちの仲間は、  
>その言葉を頭の中に思い浮かべた時には！  
>実際にコードをコミットしちまって、もうすでに終わってるからだッ！  
>だから使った事がねェーッ。  
>『コミットした』なら、使ってもいいッ！  

Overview
-------
JUnit実行のたびに、勝手にコミットしていくEclipseプラグインです。

コミットのメッセージなどはサンプルプロジェクトを参照のこと：[https://github.com/katzchang/HelloTDD](https://github.com/katzchang/HelloTDD)

Update Site
-------
https://github.com/katzchang/ProsciuttoGit/raw/master/junit.extensions.eclipse.prosciutto.site

Getting Started
-------
1. Open Preferences > ProsciuttoGit.
2. Add Author and Committer settings.
3. Run JUnit test runner in gitted project
4. Run git log at project root directory, and you'll see how it works.

Warning
-------
 * このプラグインは評価版です。意味不明でも仕方がありません。
 * Eclipse 3.6 Heliosで動くはずです。3.5以前ではわかりません。環境の検証などは不十分です。質問などは[@katzchang](http://twitter.com/katzchang)までお気軽に。
 * テストを実行するプロジェクトがgitレポジトリではない場合、恐らく、よくないことが起こります。
 * 真面目なプロジェクトへの適用は慎重であるべきです。ガリガリコミットされてしまいます。
 * たとえばmasterで作業している場合、危険かもしれません。branchを作成して移動すれば、幾分安心できるかもしれません。
 * どんなときに使えるかというと、例えば開発の流れをみせるデモをつくるとか。

License
-------
評価版であり、ライセンスは未定。
使用に際し発生した如何なる損害に対しても、作成者は責を負わない。

Thanks
-------
 * EGit Project : EGitに全面的に依存しています。
 * @kompiro : 作成に際し、[twJUnit](https://github.com/kompiro/twJUnit)を参考にさせていただきました。

Committer
-------
[@katzchang](http://twitter.com/katzchang)
