# vim:ft=ruby

guard :copy,
   :delete => true,
   :mkpath => true,
   :verbose => true,
   :from => 'src/main/webapp',
   :to => 'target/esporx_tv' do
      notification :off
      interactor :off
      ignore( /\.(xml|less)$/ )
end

guard 'less',
   :all_after_change => false,
   :output => 'target/esporx_tv/static/css' do
     notification :off
     interactor :off
     watch( /.*\.less$/ )
end
