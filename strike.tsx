import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { useSiteSettings } from "@/hooks/useSiteSettings";
import { motion } from "framer-motion";
const HeroSection = () => {
  const navigate = useNavigate();
  const {
    getSetting
  } = useSiteSettings();

  // Get background image from site settings or use original fallback
  const backgroundImage = getSetting('hero_background_image') || '/lovable-uploads/8d2115d7-e4c8-46e2-bb35-7d424a35f3cd.png';
  const handleExploreNow = () => {
    navigate('/properties');
  };
  return <motion.section initial={{
    opacity: 0
  }} animate={{
    opacity: 1
  }} transition={{
    duration: 1.2,
    ease: [0.25, 0.46, 0.45, 0.94]
  }} className="relative min-h-screen flex items-center bg-cover bg-center bg-no-repeat font-['Poppins']" style={{
    backgroundImage: `url(${backgroundImage})`
  }}>
      {/* Gradient Overlay - transparent at top to solid at bottom */}
      <div style={{
      background: 'linear-gradient(to bottom, transparent 0%, rgba(0,0,0,0.1) 30%, rgba(0,0,0,0.6) 70%, rgba(0,0,0,0.9) 100%)'
    }} className="absolute inset-0 bg-gradient-to-b from-transparent via-black/20 to-black/70" />
      
      {/* Hero Content - Left Aligned */}
      <div className="relative z-10 w-full h-full flex items-center">
        <div className="container mx-auto px-6 lg:px-8">
          <div className="max-w-3xl my-0 -mt-[50px]">
            {/* Trust Badge */}
            <motion.div initial={{
            y: -20,
            opacity: 0
          }} animate={{
            y: 0,
            opacity: 1
          }} transition={{
            duration: 0.6,
            delay: 0.8,
            ease: [0.25, 0.46, 0.45, 0.94]
          }} className="inline-flex items-center gap-2 bg-black/30 backdrop-blur-sm border border-white/20 rounded-full px-4 py-2 mb-6">
              <span className="text-yellow-400">✨</span>
              <span className="text-white text-sm font-medium">
                Trusted by 10,000+ Happy Tenants
              </span>
            </motion.div>

            {/* Main Heading */}
            <div className="mb-6">
              <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-white leading-tight opacity-1 font-comfortaa" style={{
              whiteSpace: 'normal',
              wordBreak: 'keep-all',
              overflowWrap: 'normal'
            }}>
                Find the Key
                <br />
                to your 
                		<span className="relative inline-block align-middle">
                  <span className="relative z-0 text-white font-comfortaa font-semibold">Heart</span>
                  <svg
                    aria-hidden
                    className="absolute left-0 right-0 top-1/2 -translate-y-1/2 w-[112%] -ml-[6%] h-[0.7em] -rotate-[7deg] z-20 pointer-events-none"
                    viewBox="0 0 100 20"
                    preserveAspectRatio="none"
                  >
                    <path
                      d="M1 9 C12 4, 24 12, 36 8 C48 5, 60 11, 72 7 C84 5, 94 8, 99 9 L99 12 C90 14, 78 12, 66 13 C54 14, 42 12, 30 13 C18 14, 8 12, 1 12 Z"
                      fill="#dc2626"
                      opacity="0.95"
                    />
                  </svg>
                </span>
                <span className="text-primary font-comfortaa font-semibold"> Home</span>!
              </h1>
            </div>
            
            {/* Subheading */}
            <motion.p initial={{
            y: 30,
            opacity: 0
          }} animate={{
            y: 0,
            opacity: 1
          }} transition={{
            duration: 0.8,
            delay: 0,
            ease: [0.25, 0.46, 0.45, 0.94]
          }} style={{
            fontFamily: 'Poppins, sans-serif'
          }} className="text-base text-white/90 mb-8 max-w-2xl leading-relaxed font-thin md:text-3xl">
              No brokers. No drama. Just homes.
            </motion.p>
            
            {/* CTA Buttons */}
            <motion.div initial={{
            y: 40,
            opacity: 0
          }} animate={{
            y: 0,
            opacity: 1
          }} transition={{
            duration: 0.8,
            delay: 0,
            ease: [0.25, 0.46, 0.45, 0.94]
          }} className="flex flex-col sm:flex-row items-start gap-4">
              <motion.div initial={{
              scale: 0.8,
              opacity: 0
            }} animate={{
              scale: 1,
              opacity: 1
            }} transition={{
              duration: 0.6,
              delay: 0,
              type: "spring",
              stiffness: 120
            }} whileHover={{
              scale: 1.05,
              y: -3
            }} whileTap={{
              scale: 0.95
            }}>
                <Button size="lg" onClick={handleExploreNow} style={{
                fontFamily: 'Poppins, sans-serif'
              }} className="px-8 py-3 font-medium transition-all duration-300 bg-primary text-white hover:bg-primary/90">I Need a Place</Button>
              </motion.div>
              
              <motion.div initial={{
              scale: 0.8,
              opacity: 0
            }} animate={{
              scale: 1,
              opacity: 1
            }} transition={{
              duration: 0.6,
              delay: 0,
              type: "spring",
              stiffness: 120
            }} whileHover={{
              scale: 1.05,
              y: -3
            }} whileTap={{
              scale: 0.95
            }}>
                <Button variant="outline" size="lg" onClick={() => navigate('/listings/new')} style={{
                fontFamily: 'Poppins, sans-serif'
              }} className="px-8 py-3 text-base font-medium bg-transparent border-2 border-white text-white hover:text-white hover:border-primary transition-all duration-300 backdrop-blur-sm">I’ve Got a Place</Button>
              </motion.div>
            </motion.div>
          </div>
        </div>
      </div>
    </motion.section>;
};
export default HeroSection;