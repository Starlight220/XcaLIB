# XcaLib
A Kotlin library for FRC

The library has multiple packages, each needs to be included separately:
- **core** : Adds useful functionality for users who use only the basic features of wpilib, etc. 
Has no additional dependencies.
- **command** : A library built to add functionality to the wpilibj new command framework, and a 
testing API. Depends on wpilibj-new-commands.
- **ctre** : Adds capabilities to CTRE's TalonSRX/VictorSPX library. Depends on the Phoenix library.
- **kauai** : Adds capabilities to the NavX API. Depends on the navX-frc-java library.
- **limelight** : Adds a convenient API to interface with a Limelight vision camera. Has no 
additional dependencies.
- **rev** : Adds capabilities to the SparkMax API. Depends on the SparkMax Java library.

All libraries depend on the basic wpilibj and the Kotlin/Java standard libraries, and the **core** 
library. 
